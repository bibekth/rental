<?php

namespace App\Traits;

use App\Models\User;
use Illuminate\Support\Str;

trait TokenTrait
{
    public static function GenerateToken($length = 64){
        do {
            $token = Str::random($length); // Generate a random string
            // Check if the token already exists in the users table
            $userWithToken = User::where('remember_token', $token)->first();
        } while ($userWithToken); // Keep generating until a unique token is found

        return $token;
    }

    public static function SeperateBearer($request){
        $bearerToken = $request;
        $token = substr($bearerToken,7);
        return $token;
    }
}
