using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_imagem")]
    public partial class TbImagem
    {
        [Key]
        [Column("id_imagem")]
        public int IdImagem { get; set; }
        [Required]
        [Column("img")]
        public byte[] Img { get; set; }
        [Column("id_tarefa")]
        public int IdTarefa { get; set; }
        [Column("is_duvida")]
        public int IsDuvida { get; set; }

        [ForeignKey(nameof(IdTarefa))]
        [InverseProperty(nameof(TbTarefa.TbImagem))]
        public virtual TbTarefa IdTarefaNavigation { get; set; }
    }
}
