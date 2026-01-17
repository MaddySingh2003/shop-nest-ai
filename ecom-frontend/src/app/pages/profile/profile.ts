import { CommonModule } from "@angular/common";
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { ProfileService } from "../../services/profile.service";

@Component({
    selector:'app-profile',
    standalone:true,
    imports:[CommonModule],
    templateUrl:'./profile.html'
})
export class ProfileComponent implements OnInit{

    user:any=null;
    loading=true;

    constructor(
        private profileService:ProfileService,
        private cdr:ChangeDetectorRef
    ){}

    ngOnInit() {
        this.loadProfile();
    }

    loadProfile(){
        this.profileService.getProfile().subscribe(res=>{
            console.log("PROFILE",res);
            this.user=res;
            this.loading=false;
            this.cdr.detectChanges();
        })
    }
}