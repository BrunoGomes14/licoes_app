using System.Net.Http;
using System.Threading;

namespace apiEscola.Threads
{
    public class ThreadMantemLigado
    {
        static Thread thread = null;
        
        public ThreadMantemLigado()
        {
            ThreadStart threadStart = new ThreadStart(this.FuncaoMantemLigado);
            thread = new Thread(threadStart);
            thread.IsBackground = true;
            thread.Start();
        }

        private async void FuncaoMantemLigado()
        {
            Business.GerenciaBusiness gerencia = new Business.GerenciaBusiness();
            Models.db_escola_igorContext db = new Models.db_escola_igorContext();

            while (true)
            {
                HttpClient client = new HttpClient();

                string json = client.GetAsync("https://avisoescola.herokuapp.com/Escola/Teste")
                                .Result
                                .Content
                                .ReadAsStringAsync()
                                .Result;

                // Espera 20 minutos
                Thread.Sleep(1200000);
            }
        }
    }
}