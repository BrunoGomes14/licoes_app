using System.Collections.Generic;

namespace apiEscola.Models.Request
{
    public class TarefaRequest
    {
        public int IdTarefa { get; set; }
        public int DsEntregue { get; set; }
        public List<TbImagem> imgsTarefa {get;set;}
        public int qtdDiaVencer { get; set; }
        public bool bEntregou { get; set; }
    }
}