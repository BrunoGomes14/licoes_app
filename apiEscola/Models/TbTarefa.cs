using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_tarefa")]
    public partial class TbTarefa
    {
        public TbTarefa()
        {
            TbImagem = new HashSet<TbImagem>();
        }

        [Key]
        [Column("id_tarefa")]
        public int IdTarefa { get; set; }
        [Required]
        [Column("nm_tarefa", TypeName = "varchar(45)")]
        public string NmTarefa { get; set; }
        [Required]
        [Column("ds_tarefa", TypeName = "varchar(150)")]
        public string DsTarefa { get; set; }
        [Column("dt_entrega", TypeName = "datetime")]
        public DateTime DtEntrega { get; set; }
        [Column("ds_entregue")]
        public int? DsEntregue { get; set; }
        [Column("dt_entregou", TypeName = "datetime")]
        public DateTime? DtEntregou { get; set; }
        [Column("ds_ativo")]
        public int DsAtivo { get; set; }

        [InverseProperty("IdTarefaNavigation")]
        public virtual ICollection<TbImagem> TbImagem { get; set; }
    }
}
