using System;
using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace apiEscola.Models.Response
{
    public class ResponseHorarios
    {
        public ResponseHorarios()
        {
            Materias = new List<MateriaResponse>();
        }

        public string Dia { get; set; }
        public List<MateriaResponse> Materias { get; set; } 
        public DayOfWeek diaHorario {get; set;}

        [JsonIgnore]
        public List<string> AddMaterias
        {
            set
            {
                value.ForEach(materia => Materias.Add(new MateriaResponse(materia)));
            }
        }
    }
}