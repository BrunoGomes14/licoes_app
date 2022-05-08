using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System;
using Newtonsoft.Json.Linq;
using PushSharp.Google;
using PushSharp.Core;

namespace apiEscola.Business
{
    public class NotificacoesBusiness
    {
        public static void EnviaNotificacao(List<Models.TbUsuario> usuarios, string titulo, string subtitulo)
        {
            // Configuration FCM (use this section for FCM)
            var config = new GcmConfiguration("AAAAlnQdA3Y:APA91bFzpV_SSqOeLa8MmL3eM_ANyiQ6vMgsZy7PGPCYC8a-SQ7MLPae9lKCOjlwUwW8dnt_smSPT_HYuCfD0LEeWdk_cQIQpkxmQct-bpivB-y0bEqtJ8Ktqiqs9UH4B1woTvBRtsS8");
            config.GcmUrl = "https://fcm.googleapis.com/fcm/send";
            var provider = "FCM";

            // Create a new broker
            var gcmBroker = new GcmServiceBroker (config);

            // Wire up events
            gcmBroker.OnNotificationFailed += (notification, aggregateEx) => {
            
            	aggregateEx.Handle (ex => {
                
            		// See what kind of exception it was to further diagnose
            		if (ex is GcmNotificationException notificationException) {
                    
            			// Deal with the failed notification
            			var gcmNotification = notificationException.Notification;
            			var description = notificationException.Description;

            			Console.WriteLine ($"{provider} Notification Failed: ID={gcmNotification.MessageId}, Desc={description}");
            		} else if (ex is GcmMulticastResultException multicastException) {
                    
            			foreach (var succeededNotification in multicastException.Succeeded) {
            				Console.WriteLine ($"{provider} Notification Succeeded: ID={succeededNotification.MessageId}");
            			}

            			foreach (var failedKvp in multicastException.Failed) {
            				var n = failedKvp.Key;
            				var e = failedKvp.Value;

            				Console.WriteLine ($"{provider} Notification Failed: ID={n.MessageId}, Desc={e.Message}");
            			}

            		} else if (ex is DeviceSubscriptionExpiredException expiredException) {
                    
            			var oldId = expiredException.OldSubscriptionId;
            			var newId = expiredException.NewSubscriptionId;

            			Console.WriteLine ($"Device RegistrationId Expired: {oldId}");

            			if (!string.IsNullOrWhiteSpace (newId)) {
            				// If this value isn't null, our subscription changed and we should update our database	
            				Console.WriteLine ($"Device RegistrationId Changed To: {newId}");
            			}
            		} else if (ex is RetryAfterException retryException) {
                    
            			// If you get rate limited, you should stop sending messages until after the RetryAfterUtc date
            			Console.WriteLine ($"{provider} Rate Limited, don't send more until after {retryException.RetryAfterUtc}");
            		} else {
            			Console.WriteLine ("{provider} Notification Failed for some unknown reason");
            		}

            		// Mark it as handled
            		return true;
            	});
            };

            gcmBroker.OnNotificationSucceeded += (notification) => {
            	Console.WriteLine ("{provider} Notification Sent!");
            };

            // Start the broker
            gcmBroker.Start ();

            foreach (var regId in usuarios) 
            {
                JObject notification = JObject.Parse ("{\"title\": \"" + titulo +"\",\"body\": \"" + subtitulo + "\"}");


            	// Queue a notification to send
            	gcmBroker.QueueNotification (new GcmNotification {
            		RegistrationIds = new List<string> { 
            			regId.DsChaveFirebase
            		},
                    Data = notification
            	});
            } 

            // Stop the broker, wait for it to finish   
            // This isn't done after every message, but after you're
            // done with the broker
            gcmBroker.Stop ();
        }
    }
}