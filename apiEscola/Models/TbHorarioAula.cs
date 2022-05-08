using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_horario_aula")]
    public partial class TbHorarioAula
    {
        [Key]
        [Column("id_horario_aula")]
        public int IdHorarioAula { get; set; }
        [Column("hr_inicio", TypeName = "time")]
        public TimeSpan HrInicio { get; set; }
        [Column("hr_fim", TypeName = "time")]
        public TimeSpan HrFim { get; set; }
        [Column("dt_ultimo_aviso", TypeName = "datetime")]
        public DateTime DtUltimoAviso { get; set; }
        [Column("dt_ultimo_receb_confirmado", TypeName = "datetime")]
        public DateTime? DtUltimoRecebConfirmado { get; set; }
    }
}
