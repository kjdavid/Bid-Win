import { Component, OnInit } from '@angular/core';
import { ItemService } from '../../../item.service';
import { Item } from '../../../item';
import { AuthService } from '../../../auth.service';

@Component({
  selector: 'app-itemlist',
  templateUrl: './itemlist.component.html',
  styleUrls: ['./itemlist.component.css']
})
export class ItemlistComponent implements OnInit {
  items: Item[];
  itemid: number;
  constructor(
    private itemService: ItemService,
    private authService: AuthService,
  ) {
    if(this.authService.isLoggedIn()){
      this.itemService.fuseItemsBidsImages();
    }
    this.itemService.listItems("");
    this.items = this.itemService.getListedItems();
  }

  ngOnInit() {
  }
  setChosenItem(itemid:number){
    this.itemService.setChosenItem(itemid);
  }
  makebid(bid){
    console.log(bid);
  }
  vege(id:number):boolean{
    for(let i = 0; i < this.items.length; i++){
      if(this.items[i].id===id){
        return this.authService.time.valueOf()>this.items[i].endTime.valueOf();
      }
    }
    return false;
  }
}
