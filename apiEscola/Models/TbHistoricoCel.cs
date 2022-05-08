using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_historico_cel")]
    public partial class TbHistoricoCel
    {
        [Key]
        [Column("id_historico_cel")]
        public int IdHistoricoCel { get; set; }
        [Required]
        [Column("ds_app_nome", TypeName = "varchar(550)")]
        public string DsAppNome { get; set; }
        [Column("ds_horario", TypeName = "datetime")]
        public DateTime DsHorario { get; set; }
        [Required]
        [Column("ds_bateria", TypeName = "varchar(85)")]
        public string DsBateria { get; set; }
    }
}
