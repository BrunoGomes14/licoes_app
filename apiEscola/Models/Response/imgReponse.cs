namespace apiEscola.Models.Response
{
    public class imgReponse
    {
        public int IdImagem { get; set; }
        public byte[] Img { get; set; }
        public int IdTarefa { get; set; }
        public int IsDuvida { get; set; }
    }
}