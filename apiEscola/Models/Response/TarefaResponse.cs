using System.Collections.Generic;
using System;

namespace apiEscola.Models.Response
{
    public class TarefaResponse
    {
        public int idTarefa { get; set; }
        public string nmTarefa { get;set; }
        public string dsTarefa { get; set; }
        public DateTime dtEntrega { get; set; }
        public List<imgReponse> imgs { get; set; }
        public int qtdDiaVencer { get; set;}
        public bool bEntregou { get; set; }
    }
}