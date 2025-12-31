export interface LoginReguest{
    email:string;
    password:string;
}

export interface AuthResponse{
    token:string;
    email:string;
    role:'USER'|'ADMIN';
    expiresAt:number;
}