using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace apiEscola.Models
{
    public partial class db_escola_igorContext : DbContext
    {
        public db_escola_igorContext()
        {
        }

        public db_escola_igorContext(DbContextOptions<db_escola_igorContext> options)
            : base(options)
        {
        }

        public virtual DbSet<TbHistoricoCel> TbHistoricoCel { get; set; }
        public virtual DbSet<TbHorarioAula> TbHorarioAula { get; set; }
        public virtual DbSet<TbImagem> TbImagem { get; set; }
        public virtual DbSet<TbMaterias> TbMaterias { get; set; }
        public virtual DbSet<TbSemanaMateria> TbSemanaMateria { get; set; }
        public virtual DbSet<TbTarefa> TbTarefa { get; set; }
        public virtual DbSet<TbUsuario> TbUsuario { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseMySql("server=us-cdbr-east-03.cleardb.com;user id=bfe9fa9b4c6749;password=2ac12c14;database=heroku_ac0a69cfe437947", x => x.ServerVersion("8.0.23-mysql"));
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<TbHistoricoCel>(entity =>
            {
                entity.HasKey(e => e.IdHistoricoCel)
                    .HasName("PRIMARY");

                entity.Property(e => e.DsAppNome)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");

                entity.Property(e => e.DsBateria)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");
            });

            modelBuilder.Entity<TbHorarioAula>(entity =>
            {
                entity.HasKey(e => e.IdHorarioAula)
                    .HasName("PRIMARY");

                entity.Property(e => e.IdHorarioAula).ValueGeneratedNever();
            });

            modelBuilder.Entity<TbImagem>(entity =>
            {
                entity.HasKey(e => e.IdImagem)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.IdTarefa)
                    .HasName("fk_ta_idx");

                entity.HasOne(d => d.IdTarefaNavigation)
                    .WithMany(p => p.TbImagem)
                    .HasForeignKey(d => d.IdTarefa)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_ta");
            });

            modelBuilder.Entity<TbMaterias>(entity =>
            {
                entity.HasKey(e => e.IdMateria)
                    .HasName("PRIMARY");

                entity.Property(e => e.DsNome)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");
            });

            modelBuilder.Entity<TbSemanaMateria>(entity =>
            {
                entity.HasKey(e => e.IdSemanaMateria)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.IdMateria)
                    .HasName("fk_id_mateira_idx");

                entity.Property(e => e.IdSemanaMateria).ValueGeneratedNever();

                entity.HasOne(d => d.IdMateriaNavigation)
                    .WithMany(p => p.TbSemanaMateria)
                    .HasForeignKey(d => d.IdMateria)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("fk_id_mateira");
            });

            modelBuilder.Entity<TbTarefa>(entity =>
            {
                entity.HasKey(e => e.IdTarefa)
                    .HasName("PRIMARY");

                entity.Property(e => e.DsAtivo).HasDefaultValueSql("'1'");

                entity.Property(e => e.DsEntregue)
                    .HasDefaultValueSql("'0'")
                    .HasComment("0 - pendente \\n1 - entregue \\n2 - cancelado \\n3 - com duvida");

                entity.Property(e => e.DsTarefa)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");

                entity.Property(e => e.NmTarefa)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");
            });

            modelBuilder.Entity<TbUsuario>(entity =>
            {
                entity.HasKey(e => e.IdUsuario)
                    .HasName("PRIMARY");

                entity.Property(e => e.DsChaveFirebase)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");

                entity.Property(e => e.NmUsuario)
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
