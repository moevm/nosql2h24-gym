import axiosInstance from "../axios";

export enum Roles {
	ROLE_ADMIN = "ROLE_ADMIN",
	ROLE_TRAINER = "ROLE_TRAINER",
	ROLE_USER = "ROLE_USER",
}

export function getRoleName(role: Roles): string {
	switch (role) {
		case Roles.ROLE_ADMIN:
			return "Администратор";
		case Roles.ROLE_TRAINER:
			return "Тренер";
		case Roles.ROLE_USER:
			return "Пользователь";
		default:
			return "Неизвестная роль";
	}
}

export interface IUserData {
	email: string;
	roles: Roles[];
	id: string;
}

export class AuthController {
	// Хранение информации о текущем пользователе
	private user: IUserData | null = null;
	private authToken: string | null = null;
	
	constructor() {
		// Инициализация токена и данных пользователя при создании экземпляра
		const token = localStorage.getItem("authToken");
		if (token) {
			this.authToken = token;
			this.loadUserDataFromToken(token);
		}
	}
	
	// Функция для декодирования JWT токена
	private decodeJWT(token: string): any {
		const base64Url = token.split(".")[1];
		const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
		const jsonPayload = decodeURIComponent(
			atob(base64)
				.split("")
				.map((c) => `%${("00" + c.charCodeAt(0).toString(16)).slice(-2)}`)
				.join("")
		);
		return JSON.parse(jsonPayload);
	}
	
	// Функция для проверки срока действия токена
	private isTokenExpired(token: string): boolean {
		const decodedToken = this.decodeJWT(token);
		const expirationTime = decodedToken.exp * 1000; // Конвертируем в миллисекунды
		const currentTime = Date.now();
		return currentTime >= expirationTime;
	}
	
	// Функция для загрузки данных о пользователе из токена
	public loadUserDataFromToken(token: string): void {
		try {
			const decodedToken = this.decodeJWT(token);
			this.user = {
				email: decodedToken.email,
				roles: decodedToken.roles,
				id: decodedToken.sub
			};
		} catch (error) {
			console.error("Ошибка при декодировании токена:", error);
			this.logout();
		}
	}
	
	// Получаем токен из кэша (если он есть) или из localStorage
	private getStoredAuthToken(): string | null {
		if (this.authToken && !this.isTokenExpired(this.authToken)) {
			return this.authToken;
		}
		const token = localStorage.getItem("authToken");
		if (token && !this.isTokenExpired(token)) {
			this.authToken = token;
			return token;
		}
		return null;
	}
	
	// Функция для получения токена
	public getAuthToken(): string | null {
		return this.getStoredAuthToken();
	}
	
	public getUserData(): IUserData | null {
		return this.user;
	}
	
	public getUserRoles(): Roles[] | undefined {
		return this.user?.roles;
	}
	
	// Функция для установки токена в localStorage и кэширование
	public setAuthToken(token: string): void {
		localStorage.setItem("authToken", token);
		this.authToken = token;
		this.loadUserDataFromToken(token);
	}
	
	// Функция для удаления токенов и сброса состояния
	public logout(): void {
		localStorage.removeItem("authToken");
		localStorage.removeItem("refreshToken");
		this.authToken = null;
		this.user = null;
	}
	
	// Обновление токена
	public async refreshAuthToken(): Promise<string | null> {
		const refreshToken = localStorage.getItem("refreshToken");
		if (!refreshToken) {
			this.logout();
			return null;
		}
		
		try {
			// Отправляем запрос на обновление токена
			const response = await axiosInstance.post(`/refresh-token`, {
				refreshToken,
			});
			
			const { accessToken, refreshToken: newRefreshToken } = response.data;
			
			// Сохраняем новый токен в localStorage и обновляем кэш
			this.setAuthToken(accessToken);
			if (newRefreshToken) {
				localStorage.setItem("refreshToken", newRefreshToken);
			}
			
			return accessToken;
		} catch (error) {
			console.error("Ошибка при обновлении токена", error);
			this.logout();
			return null;
		}
	}
}
