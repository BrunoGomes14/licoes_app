using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using apiEscola.Models;
using apiEscola.Models.Response;
using System.Diagnostics;

namespace apiEscola.Controllers
{        
    [ApiController]
    [Route("[controller]")]
    public class EscolaController : ControllerBase
    {
        private static DateTime dtUltimaNotificacaoRecebida;

        Business.GerenciaBusiness buGerenciamento = new Business.GerenciaBusiness();

        [HttpPost("RegistraUsuario")]
        public ActionResult adicionaUsuario(Models.TbUsuario usuario)
        {
            try
            {
                buGerenciamento.adicionaUsuario(usuario);
                return Ok();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpPost("RegistraTarefa")]
        public ActionResult adicionaTarefa(Models.TbTarefa tarefa)
        {
            try
            {
                buGerenciamento.adicionaTarefa(tarefa);
                return Ok();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpGet("ConsultaTarefas")]
        public ActionResult<List<TarefaResponse>> ConsultaTarefas()
        {
            try
            {
                return buGerenciamento.BuscaTarefas();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpPut("AlteraTarefa")]
        public ActionResult AlteraaUsuario(Models.TbTarefa tarefa)
        {
            try
            {
                buGerenciamento.AtualizaTarefa(tarefa);
                return Ok();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpPut("EntregaTarefa")]
        public ActionResult EntregaTarefa(TarefaResponse tarefa)
        {
            try
            {
                buGerenciamento.EntregaTarefa(tarefa);
                return Ok();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpGet("Teste")]
        public string notifica()
        {   
            return "Ok";
        }

        [HttpGet("IniciaThread")]
        public void teste()
        {
            new Threads.ThreadAviso();
        }

        [HttpPost("AdicionaHistorico")]
        public void AdicionaHistorico(Models.TbHistoricoCel historicoCel)
        {
            // não uso try catch pois é enviado por um serviço que não espera o retorno e não estou usando logs
            buGerenciamento.RegistraHistorico(historicoCel);
        }

        [HttpPut("ConfirmaRecebimentoNotf")]
        public void ConfirmaRecebimentoNotf()
        {
            buGerenciamento.AtualizaUltimoAvisoConf(1);
        }

        [HttpGet("AulasSemana")]
        public ActionResult<List<Models.Response.ResponseHorarios>> AulasSemana()
        {
            try
            {
                return buGerenciamento.RetornaAulasSemana();
            }
            catch (ArgumentException error)
            {
                ErrorModel erro = new ErrorModel(500, error.Message);
                return StatusCode(500, erro);
            }
            catch (Exception ex)
            {
                ErrorModel erro = new ErrorModel(500, ex.Message);
                return StatusCode(500, erro);
            }
        }

        [HttpPost("SalvaImg")]
        public ActionResult SalvaImg(byte[] img)
        {
           return Ok();
        }
    }
}