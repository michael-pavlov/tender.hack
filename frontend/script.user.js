// ==UserScript==
// @name        New script 
// @namespace   Violentmonkey Scripts
// @match       https://zakupki.mos.ru/**
// @grant       none
// @version     1.0
// @author      -
// @description 1/25/2020, 9:00:42 PM
// ==/UserScript==

let href = window.location.href;
let intereval = null;

f = function(){
  let f1, f2, f3, f4, f5 = false;
  let toInsert = null;
  
  async function success(res){
    let r = await res.json();
    if(r.charAt){
      r = JSON.parse(r);
    }
    for(let i=0; i<r.length; ++i){
      let current = window.location.pathname.substring(window.location.pathname.lastIndexOf('/')+1);
      if(String(r[i].cte)===current){
        --i;
        r.splice(i,1);
      }
    }
    console.log(r);
    let toAdd = [];
    r.length == Math.min(r.length,3);
    for(let i=0; i<r.length; ++i){
      let it = document.createElement('a');
      it.href = window.location.pathname.substring(0, window.location.pathname.lastIndexOf('/')+1)+r[i].cte;
      it.style.color = 'inherit';
      it.style.width = '300px';
      it.style.height = '150px';
      it.style.display='inline-block';
      it.style['margin-top'] = '15px'
      let name = document.createElement('div');
      name.innerText = r[i].name;
      name.style['font-size'] = '1.2em';
      name.style['font-weight'] = '600';
      it.appendChild(name);
      let img = document.createElement('img');
      img.src = r[i].img;
      img.style.width = '132px';
      img.style.height = '132px';
      img.style['padding-top'] = '5px';
      img.style['padding-right'] = '5px';
      img.style.display='inline-block';
      it.appendChild(img);
      let text = document.createElement('div');
      text.style['vertical-align'] = 'top'
      text.style['padding-top'] = '5px';
      text.style['padding-right'] = '5px';
      text.style.width = '150px';
      text.style.display='inline-block';
      let price = document.createElement('div');
      price.innerText = Number(r[i].price).toFixed(2) + ' руб.';
      price.style['font-size'] = '1.2em';
      price.style['font-weight'] = '600';
      text.appendChild(price);
      let br = document.createElement('br');
      text.appendChild(br);
      let reason = document.createElement('div');
      reason.innerText = r[i].reason;
      text.appendChild(reason);
      it.appendChild(text);
      // let toPage = document.createElement('span');
      // toPage.style['text-decoration'] = 'underline';
      // toPage.style['vertical-align'] = 'bottom';
      // toPage.innerText = 'К заказу';
      // it.appendChild(toPage);
      toAdd.push(it);
    }
    toInsert = toAdd;
    console.log(r);
  }
  
  function error(e){
    success({json:function(){
      return [
        {
          img: 'https://i.ytimg.com/vi/du2begoIXNc/maxresdefault.jpg',
          name: 'Kitty 1',
          reason:'cute',
          price: 1,
          cte: '1234'
        },{
          img:'http://i.ytimg.com/vi/Uk1RPEQI8mI/maxresdefault.jpg',
          name: 'Kitty 2',
          reason:'also cute',
          price: 1,
          cte: '4321'
        },{
          img:'https://www.publicdomainpictures.net/pictures/30000/nahled/cat-licking-lips.jpg',
          name: 'Kitty 3',
          reason:'cute as well',
          price: 1,
          cte: '1423'
        }
      ]
    }})
  }
  
  interval = setInterval(()=>{
    if(window.location.href !== href){
      f1=false;
      f2=false;
      f3=false;
      f4=false;
      f5=false;
      toInsert = null;
    }
    if(!href.match('https://zakupki.mos.ru/sku/view/.*')){
      return;
    }
    if(!f1){
      f1=true;
      let id = window.location.pathname.substring(window.location.pathname.lastIndexOf('/')+1);
      fetch('http://localhost:9080/api/tender/get-suggestion',{
          method: 'POST',
          headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({id})})
        .then(success)
        .catch(error)
    }
    if(!f2){
      let el = document.querySelector('.ui.grid > div.row:nth-child(2) > div > div');
      if(el){
        f2=true;
        el.innerText=el.innerText.replace('\n',' / ');
      }
    }
    if(!f3 && f2 && toInsert){
      let el = document.querySelector('.ui.grid > div.row:nth-child(2) > div > div');
      for(let i=0; i<3; ++i){
        if(el){
          el = el.parentElement;
        }
      }
      if(el){
          console.log(toInsert);
        for(let i = 0; i<toInsert.length || 0; ++i){
          el.parentElement.appendChild(toInsert[i]);
        }
        if(!toInsert.length && toInsert.length!==0){
          el.parentElement.appendChild(toInsert);
        }
        f3=true;
      }
    }
    if(!f4){
      // let firstOrder = document.querySelector('.SkuViewStyles__TabContainer-fp8xkv-16 > div:nth-child(2) > div:nth-child(2)');
      let firstOrder = document.querySelector('.CartButtonStyles__AddToCartButton-e9lrdu-1')
      if(firstOrder){
        f4 = true;
        firstOrder.innerText = 'Присоединиться к совместной закупке';
        firstOrder.style['padding-left'] = '7px';
        firstOrder.style.width = '250px';
        firstOrder.style.height = '50px';
        firstOrder.style.transform = 'translateX(-55px)';
        let info = document.createElement('span');
        info.style.float = 'right';
        info.style.transform = 'translateY(-60px)';
        info.style['font-size'] = '16px';
        info.style.color = '#dd9999';
        info.style['margin-left'] = '900px'
        info.style['z-index'] = 1000;
        info.style['position'] = 'absolute';
        info.innerText = 'На этот товар есть совместная закупка'
        let h = document.querySelector('h1');
        h.appendChild(info);
        console.log(h);
        console.log(info);
        // console.log(firstOrder);
        // f4=true;
        // let div = document.createElement('div');
        // div.class = 'middle aligned row SkuOffersStyles__OfferRow-sc-11rqp4x-5 cMbYBz';
        // div.innerHTML = '<div class="column"><a href="https://old.zakupki.mos.ru/#/suppliers/1318215" class="SkuOffersStyles__OfferSupplierLink-sc-11rqp4x-6 dSfepw">Индивидуальный предприниматель Сычев Александр Анатольевич </a><span class="SkuOffersStyles__OfferDescription-sc-11rqp4x-8 eWehEU"></span><a href="https://old.zakupki.mos.ru/#/suppliers/1318215" class="SkuOffersStyles__OfferDescriptionLink-sc-11rqp4x-7 iwDJGp">ИНН: 771673321130</a><a href="https://old.zakupki.mos.ru/#/offers/787130893" class="SkuOffersStyles__OfferDescriptionLink-sc-11rqp4x-7 iwDJGp">Оферта: №4514485-19</a></div><div class="column"><span class="SkuOffersStyles__OfferDescription-sc-11rqp4x-8 eWehEU">Сроки поставки: 1 - 1 дней</span><span class="SkuOffersStyles__OfferDescription-sc-11rqp4x-8 eWehEU">Регион поставки: г Москва</span></div><div class="column"><span class="SkuOffersStyles__OfferDescription-sc-11rqp4x-8 eWehEU">Без НДС</span></div><div class="column"><a href="https://old.zakupki.mos.ru/#/offers/787130893" class="SkuOffersStyles__OfferCost-sc-11rqp4x-9 kdRIaU">79&nbsp;990&nbsp;руб.</a></div><div class="column"><button class="ui basic button CartButtonStyles__IncreaseButton-e9lrdu-0 CartButtonStyles__AddToCartButton-e9lrdu-1 fLMcfe">Присоединиться к общей покупке</button></div>';
        // firstOrder.parentElement.insertBefore(div, firstOrder);
      }
    }
    if(!f5){
      var ee = document.querySelectorAll('.SkuViewStyles__MainInfoHeader-fp8xkv-4');
      for(let i=0; i<ee.length; ++i){
        if(ee[i].innerText.startsWith('Предложения поставщиков отсутствуют')){
          let e = ee[i].parentElement;
          f5=true;
          e.innerHTML = '<div class="ui huge header SkuViewStyles__MainInfoPriceHeader-fp8xkv-8 jtSyAK">33&nbsp;999&nbsp;руб. / штука</div>'
          e.parentElement.parentElement.children[2].children[0].innerHTML = '<div class="ui header">от 33&nbsp;999&nbsp;руб. до 33&nbsp;999&nbsp;руб.<div class="sub header">на основе 1 предложения поставщиков</div></div>'
        }
      }
    }
  },400) 
}

setInterval(()=>{
  if(window.location.href!==href){
    console.log('new href '+href);
    href = window.location.href;
    clearInterval(interval);
    f();
  }
},500)

f();