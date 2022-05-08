using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace apiEscola.Models
{
    [Table("tb_usuario")]
    public partial class TbUsuario
    {
        [Key]
        [Column("id_usuario")]
        public int IdUsuario { get; set; }
        [Column("nm_usuario", TypeName = "varchar(45)")]
        public string NmUsuario { get; set; }
        [Column("ds_chave_firebase", TypeName = "text")]
        public string DsChaveFirebase { get; set; }
        [Column("ds_tipo_usuario")]
        public int? DsTipoUsuario { get; set; }
    }
}
