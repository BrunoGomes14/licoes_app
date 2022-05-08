using System.Threading;
using System.Collections.Generic;
using System.Linq;
using System;
using Newtonsoft.Json.Linq;
using PushSharp.Google;
using PushSharp.Core;
using System.Diagnostics;

namespace apiEscola.Threads
{
    public class ThreadAviso
    {
        static Thread thread = null;
        public ThreadAviso()
        {
            ThreadStart threadStart = new ThreadStart(this.FuncaoEnviaAviso);
            thread = new Thread(threadStart);
            thread.IsBackground = true;
            thread.Start();
        }

        private void FuncaoEnviaAviso()
        {
            bool bEnviaResp = false;

            while (true)
            {
                Models.db_escola_igorContext db = new Models.db_escola_igorContext();
                Business.GerenciaBusiness gerencia = new Business.GerenciaBusiness();

                // 1 hora * 2 = 2 horas
                Thread.Sleep(3600000 * 2);
                var list = new List<Models.TbUsuario>();

                List<Models.TbTarefa> tarefas = new List<Models.TbTarefa>();

                if (gerencia.DataAtual().Hour >= 14)
                {
                    tarefas = db.TbTarefa.Where(x => x.DtEntrega.Date == (gerencia.DataAtual().AddDays(1)).Date && x.DsAtivo == 1 && x.DsEntregue == 0).ToList();
                }
                else 
                {
                    tarefas.Clear();
                }

                if (tarefas.Count > 0)
                {
                    List<Models.TbUsuario> usuarios = db.TbUsuario.Where(x => x.DsTipoUsuario > 0).ToList();
                    Business.NotificacoesBusiness.EnviaNotificacao(usuarios.Where(x => x.DsTipoUsuario == 1).ToList(), "Você tem tarefa(s) para amanhã!", "São " + tarefas.Count + " tarefa(s) com data de entrega para amanhã!");
                    
                    if (bEnviaResp)
                    {
                        Business.NotificacoesBusiness.EnviaNotificacao(usuarios.Where(x => x.DsTipoUsuario > 1).ToList(), "Lição sem fazer!", "O Igor possui " + tarefas.Count + " tarefa(s) sem fazer com data de entrega para amanhã!");
                        bEnviaResp = false;
                    }
                    else 
                    {
                        bEnviaResp = true;
                    }
                }

                // DateTime tempoAtual = DateTime.Now;
                // DateTime horarioDesligamento = Convert.ToDateTime("20:00:00");
                // TimeSpan tempoRestante = horarioDesligamento.Subtract(tempoAtual);

                // if (tempoRestante.TotalMinutes < 30)
                // {
                //     Business.NotificacoesBusiness.EnviaNotificacao(db.TbUsuario.Where(x => x.DsTipoUsuario > 1).ToList(), "Desligamento automático", "Conforme a pré-definição, o servidor se auto desligará em 5 minutos!");

                //     // define shutdown
                //     Process.Start("shutdown","/s /t 300");

                // }
            }
        }
    }
}