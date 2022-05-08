using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_materias")]
    public partial class TbMaterias
    {
        public TbMaterias()
        {
            TbSemanaMateria = new HashSet<TbSemanaMateria>();
        }

        [Key]
        [Column("id_materia")]
        public int IdMateria { get; set; }
        [Required]
        [Column("ds_nome", TypeName = "varchar(95)")]
        public string DsNome { get; set; }

        [InverseProperty("IdMateriaNavigation")]
        public virtual ICollection<TbSemanaMateria> TbSemanaMateria { get; set; }
    }
}
