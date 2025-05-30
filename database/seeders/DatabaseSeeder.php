<?php

namespace Database\Seeders;

use App\Models\User;
// use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        $this->call([
            RoleSeeder::class
        ]);
        
        $user = User::create([
            'name' => 'admin',
            'email' => 'admin@rental.com',   
            'password'=> Hash::make('password'),
            'phonenumber' => '9800000000'
        ]);

        $user->assignRole('admin');

        $userOne = User::create([
            'name' => 'User One',
            'email' => 'userone@rental.com',   
            'password'=> Hash::make('password'),
            'phonenumber' => '9800000001'
        ]);
        $userTwo = User::create([
            'name' => 'User Two',
            'email' => 'usertwo@rental.com',   
            'password'=> Hash::make('password'),
            'phonenumber' => '9800000002'
        ]);
        $userOne->assignRole('User');
        $userTwo->assignRole('User');
        
    }
}
