namespace apiEscola.Models.Response
{
    public class MateriaResponse
    {
        public MateriaResponse(string nomeMateria)
        {
            this.Materia = nomeMateria;
        }

        public string Materia { get; set; }
        public bool PossuiLicao { get; set; }
    }
}