using System;
using System.Threading;
using System.Linq;
using System.Net.Http;
using System.Collections.Generic;

namespace apiEscola.Threads
{
    public class ThreadAvisoHorarioAula
    {
        static Thread thread = null;
        public ThreadAvisoHorarioAula()
        {
            ThreadStart threadStart = new ThreadStart(this.FuncaoEnviaAviso);
            thread = new Thread(threadStart);
            thread.IsBackground = true;
            thread.Start();
        }

        private void FuncaoEnviaAviso()
        {
            Business.GerenciaBusiness gerencia = new Business.GerenciaBusiness();
            Models.db_escola_igorContext db = new Models.db_escola_igorContext();
            List<DayOfWeek> listDiasAula = null;

            listDiasAula = new List<DayOfWeek>();
            //listDiasAula.Add(DayOfWeek.Sunday);
            listDiasAula.Add(DayOfWeek.Monday);
            listDiasAula.Add(DayOfWeek.Tuesday);
            listDiasAula.Add(DayOfWeek.Wednesday);
            listDiasAula.Add(DayOfWeek.Thursday);
            listDiasAula.Add(DayOfWeek.Friday);
            //listDiasAula.Add(DayOfWeek.Saturday);

            while (true)
            {
                // Espera um minuto e meio
                Thread.Sleep(78000);

                Console.WriteLine("THREAD AVISO AULA - EXECUTOU");
            
                var horarioAula =  gerencia.BuscaHorarioAula();

                // Se o o dia atual estiver não lista de dias de aula ou for o dia mas não precisa mais avisar
                if (!listDiasAula.Contains(gerencia.DataAtual().DayOfWeek) || (listDiasAula.Contains(gerencia.DataAtual().DayOfWeek) && horarioAula == null))
                {
                    int qtdDias = 0;

                    while(true)
                    {
                        qtdDias++;

                        if (listDiasAula.Contains(gerencia.DataAtual().AddDays(qtdDias).DayOfWeek))
                        {
                            break;
                        }
                    }

                    // recupera a data atual 
                    DateTime dtAtual = gerencia.DataAtual();

                    // cria um datetime com a data e horário que a thread precisa ligar 
                    DateTime dtTempoEspera = new DateTime(dtAtual.Year, dtAtual.Month, dtAtual.AddDays(qtdDias).Day, 7, 40 , 0);

                    // tira a diferença entre esses dias                    
                    TimeSpan tps = dtTempoEspera.Subtract(dtAtual);

                    Console.WriteLine($"THREAD AVISO AULA - BOTOU PRA NANA POR {tps.TotalHours}hrs");

                    // bota a thread pra nanar
                    Thread.Sleep(tps);        

                    Console.WriteLine("THREAD AVISO AULA - ACORDOU ");
                }

                // Busca se possui algum aviso de aula
                if (horarioAula != null)
                {
                    // busca usuarios e já manda notificações
                    Business.NotificacoesBusiness.EnviaNotificacao(db.TbUsuario.Where(x => x.DsTipoUsuario == 3 || x.DsTipoUsuario == 1).ToList(), "Tá tendo aula hein!", "Tava vendo aqui e já tem aula acontecendo !");
                    
                    // atualiza o aviso de aula
                    gerencia.AtualizaUltimoAviso(1);          
                }
            }
        }
    }
}