using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_semana_materia")]
    public partial class TbSemanaMateria
    {
        [Key]
        [Column("id_semana_materia")]
        public int IdSemanaMateria { get; set; }
        [Column("id_dia")]
        public int IdDia { get; set; }
        [Column("id_materia")]
        public int IdMateria { get; set; }

        [ForeignKey(nameof(IdMateria))]
        [InverseProperty(nameof(TbMaterias.TbSemanaMateria))]
        public virtual TbMaterias IdMateriaNavigation { get; set; }
    }
}
