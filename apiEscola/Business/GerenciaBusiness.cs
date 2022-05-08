using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.EntityFrameworkCore;


namespace apiEscola.Business
{
    public class GerenciaBusiness
    {
        Models.db_escola_igorContext db = new Models.db_escola_igorContext();

        public void adicionaUsuario(Models.TbUsuario usuario)
        {
            if (usuario.DsChaveFirebase.Length <= 0 || usuario.DsTipoUsuario < 0 || usuario.NmUsuario.Length <= 0)
            {
                throw new ArgumentException("Dados enviados de forma incorreta");
            }

            db.Add(usuario);
            db.SaveChanges();
        }

        public void adicionaTarefa(Models.TbTarefa tarefa)
        {
            if (tarefa.NmTarefa.Length <= 0 || tarefa.DsTarefa.Length <= 0 || tarefa.DtEntrega == null)
            {
                throw new ArgumentException("Dados enviados de forma incorreta");
            }

            if (tarefa.DtEntrega < DataAtual().Date)
            {
                throw new ArgumentException("A data para entrega não pode ser maior que a atual");
            }

            db.Add(tarefa);
            db.SaveChanges();
        }

        public void AtualizaTarefa(Models.TbTarefa tarefa)
        {
            if (!db.TbTarefa.Any(x => x.IdTarefa == tarefa.IdTarefa))
            {
                throw new ArgumentException("tarefa não encontrada");
            }

            Models.TbTarefa tarefaAlt = db.TbTarefa.FirstOrDefault(x => x.IdTarefa == tarefa.IdTarefa);
            tarefaAlt.DsTarefa = tarefa.DsTarefa;
            tarefaAlt.NmTarefa = tarefa.NmTarefa;
            tarefaAlt.DtEntrega = tarefa.DtEntrega;

            db.SaveChanges();
        }

        public List<Models.Response.TarefaResponse> BuscaTarefas()
        {
            Models.Response.TarefaResponse tarefaResp = null;
            int qtdEntregues = 0;

            List<Models.TbTarefa> listTarefa = db.TbTarefa.Where(x => x.DtEntrega.Date <= DataAtual().AddDays(7)
                                                                 && x.DsAtivo == 1
                                                                 &&  (x.DtEntregou == null  || x.DtEntregou < DataAtual().AddDays(-7)))
                                                          .ToList();
            
            //listTarefa.RemoveAll(x => x.DtEntregou != null && x.DtEntregou < DataAtual().AddDays(-7));

            List<Models.Response.TarefaResponse> tarefaResponse = new List<Models.Response.TarefaResponse>();

            foreach (Models.TbTarefa tarefa in listTarefa)
            {
                if (tarefa.DsEntregue == 1)
                {
                    continue;
                    // qtdEntregues++;

                    // if (qtdEntregues > 3)
                    // {
                    //     continue;
                    // }
                }

                tarefaResp = new Models.Response.TarefaResponse();

                tarefaResp.idTarefa = tarefa.IdTarefa;
                tarefaResp.nmTarefa = tarefa.NmTarefa;
                tarefaResp.dsTarefa = tarefa.DsTarefa;
                tarefaResp.dtEntrega = tarefa.DtEntrega.Date;
                tarefaResp.qtdDiaVencer = ( tarefa.DtEntrega - DataAtual().Date).Days;
                //tarefaResp.imgs = BuscaImagens(tarefa.IdTarefa).ToListImg();
                tarefaResp.imgs = new List<Models.Response.imgReponse>();
                tarefaResp.bEntregou = tarefa.DsEntregue == 1;

                tarefaResponse.Add(tarefaResp);
            }

            return tarefaResponse.OrderBy(x => x.bEntregou).ThenBy(x => x.qtdDiaVencer).ToList();
        }

        public List<Models.TbImagem> BuscaImagens(int idTarefa)
        {
            return db.TbImagem.Where(x => x.IdTarefa ==  idTarefa).ToList();
        }

        public void RemoveTarefa(int idTarefa)
        {
            Models.TbTarefa tarefa = db.TbTarefa.FirstOrDefault(x => x.IdTarefa == idTarefa);

            db.TbTarefa.Remove(tarefa);
            db.SaveChanges();
        }

        public void EntregaTarefa(Models.Response.TarefaResponse tarefa) 
        {

            if (tarefa.imgs.Count == 0)
            {
                throw new ArgumentException("tarefa precisa ter as imagens");
            }

            if (!db.TbTarefa.Any(x => x.IdTarefa == tarefa.idTarefa))
            {
                throw new ArgumentException("tarefa não encontrada");
            }

            Models.TbTarefa tarefaAlt = db.TbTarefa.FirstOrDefault(x => x.IdTarefa == tarefa.idTarefa);

            if( tarefa.imgs.Where(x => x.IsDuvida != 1).ToList().Count > 0)
            {
                tarefaAlt.DsEntregue = 1;
                tarefaAlt.DtEntregou = DataAtual();
            }

            tarefa.imgs.ForEach(x => x.IdTarefa = tarefa.idTarefa);
            tarefa.imgs.ForEach(x => db.TbImagem.Add(x.ToImgTable()));

            db.SaveChanges();

            List<Models.TbUsuario> usuario = db.TbUsuario.Where(x => x.DsTipoUsuario > 1).ToList();

            string titulo = tarefaAlt.DsEntregue == 1 ? "Tarefa entregue" : "Uma dúvida foi atribuida a tarefa";
            string mensagem = tarefaAlt.DsEntregue == 1 ? "A tarefa '" + tarefaAlt.NmTarefa + "' foi entregue" : "A tarefa \"" + tarefaAlt.NmTarefa + "\" possui uma dúvida";


            NotificacoesBusiness.EnviaNotificacao(usuario, titulo, mensagem);
        }

        public void RegistraHistorico(Models.TbHistoricoCel historico)
        {
            db.TbHistoricoCel.Add(historico);
            db.SaveChanges();
        }

        public Models.TbHorarioAula BuscaHorarioAula()
        {
            return db.TbHorarioAula.FirstOrDefault(x => DataAtual().TimeOfDay >= x.HrInicio 
                                                     && DataAtual().TimeOfDay <= x.HrFim 
                                                     && x.DtUltimoAviso.Date != DataAtual().Date);
        }

        public Models.TbHorarioAula BuscaHorarioAulaConf()
        {
            return db.TbHorarioAula.FirstOrDefault(x => x.IdHorarioAula == 1);
        }

        

        public void AtualizaUltimoAviso(int id)
        {
            var horario = db.TbHorarioAula.FirstOrDefault(x => x.IdHorarioAula == id);
            horario.DtUltimoAviso = DataAtual();

            db.SaveChanges();
        }

        public void AtualizaUltimoAvisoVolta(int id)
        {
            var horario = db.TbHorarioAula.FirstOrDefault(x => x.IdHorarioAula == id);
            horario.DtUltimoAviso = DataAtual().AddDays(-1);

            db.SaveChanges();
        }

        public void AtualizaUltimoAvisoConf(int id)
        {
            var horario = db.TbHorarioAula.FirstOrDefault(x => x.IdHorarioAula == id);
            horario.DtUltimoRecebConfirmado = DataAtual();

            db.SaveChanges();
        }

        public DateTime DataAtual()
        {
            return DateTime.UtcNow.AddHours(-3);
        }

        public List<Models.Response.ResponseHorarios> RetornaAulasSemana()
        {
            List<Models.Response.ResponseHorarios> horarios = null;

            List<Models.TbSemanaMateria> aulas = db.TbSemanaMateria
                                                   .Include(x => x.IdMateriaNavigation)
                                                   .ToList();

            horarios = new List<Models.Response.ResponseHorarios>();

            horarios.Add(new Models.Response.ResponseHorarios{
                Dia = "Segunda-Feira",
                diaHorario = DayOfWeek.Monday,
                AddMaterias = aulas.Where(x => x.IdDia == 1).Select(x => x.IdMateriaNavigation.DsNome).ToList(),
                
            });
            
            horarios.Add(new Models.Response.ResponseHorarios{
                Dia = "Terça-Feira",
                diaHorario = DayOfWeek.Tuesday,
                AddMaterias = aulas.Where(x => x.IdDia == 2).Select(x => x.IdMateriaNavigation.DsNome).ToList(),
            });
            
            horarios.Add(new Models.Response.ResponseHorarios{
                Dia = "Quarta-Feira",
                diaHorario = DayOfWeek.Wednesday,
                AddMaterias = aulas.Where(x => x.IdDia == 3).Select(x => x.IdMateriaNavigation.DsNome).ToList(),
            });

            horarios.Add(new Models.Response.ResponseHorarios{
                Dia = "Quinta-Feira",
                diaHorario = DayOfWeek.Thursday,
                AddMaterias = aulas.Where(x => x.IdDia == 4).Select(x => x.IdMateriaNavigation.DsNome).ToList(),
            });

            horarios.Add(new Models.Response.ResponseHorarios{
                Dia = "Sexta-Feira",
                diaHorario = DayOfWeek.Friday,
                AddMaterias = aulas.Where(x => x.IdDia == 5).Select(x => x.IdMateriaNavigation.DsNome).ToList(),
            });

            var tarefas = db.TbTarefa.Where(x => x.DtEntrega >= DataAtual() && x.DtEntrega <= DataAtual().AddDays(7)).ToList();
            
            foreach (Models.Response.ResponseHorarios itemHorario in horarios)
            {
                DateTime dtAtual = DataAtual();
                DateTime dtFiltro = dtAtual;

                if (itemHorario.diaHorario != dtAtual.DayOfWeek)
                {
                    bool bAchouDia = true;
    
                    while(bAchouDia)
                    {
                        dtFiltro = dtFiltro.AddDays(1);
                        bAchouDia = itemHorario.diaHorario != dtFiltro.DayOfWeek;   
                    }
                }

                itemHorario.Dia += $" ({dtFiltro.ToString("dd/MM")})";


                foreach (Models.Response.MateriaResponse itemMateria in itemHorario.Materias)
                {
                    itemMateria.PossuiLicao = PossuiLicao(dtFiltro, itemMateria.Materia, tarefas);
                }
            }

            return horarios;
        }

        public bool PossuiLicao(DateTime dtFiltro, string materia, List<Models.TbTarefa> listTarefas)
        {
            return listTarefas.Any(x => x.DtEntrega.Date == dtFiltro.Date && x.NmTarefa.ToUpper().Contains(materia.ToUpper()));
        }

        public void SalvaImg(byte[] arrByteImg, string sPathAmbiente, string sIdImg)
        {

        }
    }
}