namespace apiEscola.Models
{
    public class ErrorModel
    {
        public int codigoErro { get; set; }
        public string mensagemErro { get; set;}

        public ErrorModel(int cd, string erro)
        {
            codigoErro = cd;
            mensagemErro = erro;
        }
    }
}