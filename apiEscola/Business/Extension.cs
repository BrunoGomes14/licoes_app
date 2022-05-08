using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;


namespace apiEscola
{
    public static class Extension
    {
        public static List<Models.Response.imgReponse> ToListImg(this List<Models.TbImagem> imgstb)
        {
            List<Models.Response.imgReponse> imgs = new List<Models.Response.imgReponse>();

            foreach ( Models.TbImagem img in imgstb )
            {
                imgs.Add(new Models.Response.imgReponse
                {
                    IdImagem = img.IdImagem,
                    Img = img.Img,
                    IdTarefa = img.IdTarefa,
                    IsDuvida = img.IsDuvida
                });
            }

            return imgs;
        }

        public static Models.TbImagem ToImgTable(this Models.Response.imgReponse imgstb)
        { 
            var img = new Models.TbImagem();
            
            img.IdImagem = imgstb.IdImagem;
            img.Img = imgstb.Img;
            img.IdTarefa = imgstb.IdTarefa;
            img.IsDuvida = imgstb.IsDuvida;
            
            return img;
        }
    }
}