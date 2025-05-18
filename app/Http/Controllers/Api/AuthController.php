<?php

namespace App\Http\Controllers\Api;

use App\Helper\ResponseHelper;
use App\Http\Controllers\Controller;
use App\Http\Requests\ForgotPasswordRequest;
use App\Http\Requests\LoginRequest;
use App\Http\Requests\RegisterRequest;
use App\Http\Requests\ResetPasswordRequest;
use App\Models\EmailVerification;
use App\Models\User;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Mail;

class AuthController extends Controller
{
    public function AddRegister(RegisterRequest $request)
    {
        // dd($request->all());
        // dd means dump and die which is used to see the request send by userr
        try {
            // yo user utya model bta ako ho hai
            $user = User::create([
                'name' => $request->name,
                'email' => $request->email,
                'password' => Hash::make($request->password),
                'phonenumber' => $request->phonenumber
            ]);
            if ($user) {
                // yo chai role assign gareko
                $user->assignRole('user');
                // return ResponseHelper::success(message:'User is saved',data:$user,statusCode:201);
                // $this->sendOtp($user);
                return ResponseHelper::success(message: 'Mail has been sent, please check your mail', data: [], statusCode: 201);
            } else {
                return ResponseHelper::errors(message: 'Unable to create user', statusCode: 422);
            }
        } catch (Exception $ex) {
            return ResponseHelper::errors(message: 'Unable to save' . $ex->getMessage(), statusCode: 500);
        }
    }



    public function login(LoginRequest $request)
    {
        // dd('Hello');
        $auth = Auth::attempt(['email' => $request->email, 'password' => $request->password]);
        // dd($auth);
        if ($auth === false) {
            return ResponseHelper::errors(message: 'Incorrect username or passsword', statusCode: 401);
        }
        $user = Auth::user();
        // dd($user);
        // if (!$user->is_verified){
        //     return ResponseHelper::errors(message:'Please verify your mail',statusCode:401);
        // }
        $token = $user->createToken('token')->plainTextToken;
        $authUser = [
            'user' => $user,
            'token' => $token
        ];
        // dd($token);
        return ResponseHelper::success(message: 'User is Logged in', data: $authUser, statusCode: 200);
    }

    public function logout()
    {
        $user = Auth::user();
        if ($user) {
            $user->currentAccessToken()->delete();
            return ResponseHelper::success(message: 'User is logged out', statusCode: 200);
        } else {
            return ResponseHelper::errors(message: 'User is not found', statusCode: 422);
        }
    }

    public function sendOtp($user)
    {
        // 6ota num ko otp pathauna with time
        $otp = rand(100000, 999999);
        $time = date('Y-m-d H:i:s');

        EmailVerification::updateOrCreate(
            // left ma vako chai databasema vako column name ho hai
            ['email' => $user->email],
            ['otp' => $otp],
            ['updated_at' => $time]
        );
        $data['email'] = $user->email;
        $data['title'] = "Mail Verification";
        $data['body'] = "Your OTP is :" . $otp;
        // yo mail chai blade file wala mail ho hai
        Mail::send('mail', ['data' => $data], function ($message) use ($data) {
            $message->to($data['email'])->subject($data['title']);
        });
    }

    public function verifiedOtp(Request $request)
    {
        try {
            $user = User::where('email', $request->input('email'))->first();
            $otpData = EmailVerification::where('otp', $request->input('otp'))->first();
            if (!$otpData) {
                return ResponseHelper::errors(message: 'Wrong otp', statusCode: 400);
            } else {

                $currentTime = strtotime(date('Y-m-d H:i:s'));
                $time = strtotime($otpData->updated_at);


                if ($currentTime >= $time && $time >= $currentTime - (90 + 5)) {
                    User::where('id', $user->id)->update([
                        'is_verified' => 1
                    ]);
                    return ResponseHelper::success(message: 'Mail Verified ', data: $user, statusCode: 200);
                } else {
                    return ResponseHelper::errors(message: 'Mail Expired ', statusCode: 400);
                }
            }
        } catch (Exception $e) {
            return ResponseHelper::errors(message: $e->getMessage(), statusCode: 500);
        }
    }
    public function resendOtp(Request $request)
    {

        $user = User::where('email', $request->input('email'))->first();
        $otpData = EmailVerification::where('email', $request->input('email'))->first();

        $currentTime = strtotime(date('Y-m-d H:i:s'));
        $time = strtotime($otpData->updated_at);
        $timeDifference = $currentTime - $time;

        if ($timeDifference < 95) { //90 seconds
            return ResponseHelper::errors(message: 'Please Wait ', statusCode: 400);
        } else {
            $this->sendOtp($user); //OTP SEND
            return ResponseHelper::success(message: 'OTP has sent ', statusCode: 200);
        }
    }

    public function forgotPassword(ForgotPasswordRequest $request)
    {
        // dd('hi');
        try {
            $user = User::where('email', $request->email)->first();

            if (!$user) {
                return ResponseHelper::errors('Email not found', 404);
            }

            $this->sendOtp($user);
            return ResponseHelper::success('OTP has been sent to your email', [], 200);
        } catch (Exception $ex) {
            return ResponseHelper::errors('Error sending OTP: ' . $ex->getMessage(), 500);
        }
    }

    public function resetPassword(ResetPasswordRequest $request)
    {
        try {
            $otpData = EmailVerification::where('otp', $request->otp)->first();

            if (!$otpData || $otpData->email !== $request->email) {
                return ResponseHelper::errors('Invalid OTP', 400);
            }

            $currentTime = now()->timestamp;
            $otpTime = strtotime($otpData->updated_at);

            if ($currentTime > $otpTime + 90) {
                return ResponseHelper::errors('OTP expired', 400);
            }

            $user = User::where('email', $request->email)->first();
            $user->password = Hash::make($request->password);
            $user->save();


            $otpData->delete();

            return ResponseHelper::success('Password has been reset successfully', [], 200);
        } catch (Exception $ex) {
            return ResponseHelper::errors('Error resetting password: ' . $ex->getMessage(), 500);
        }
    }


    // public function updateProfile(Request $request)
    // {
    //     try {
    //         // Validate the request data
    //         $request->validate([
    //             'name' => 'sometimes|string|max:255',
    //             'email' => 'sometimes|email|unique:users,email,' . Auth::id(),
    //             'phonenumber' => 'sometimes|string|max:20',
    //             'password' => 'sometimes|confirmed|min:8',
    //         ]);

    //         // Retrieve the authenticated user
    //         $user = Auth::user();

    //         // Update user profile data
    //         $user->name = $request->input('name', $user->name);
    //         $user->email = $request->input('email', $user->email);
    //         $user->phonenumber = $request->input('phonenumber', $user->phonenumber);

    //         if ($request->filled('password')) {
    //             $user->password = Hash::make($request->password);
    //         }

    //         $user->save();

    //         return ResponseHelper::success(message: 'Profile updated successfully', data: $user, statusCode: 200);
    //     } catch (Exception $ex) {
    //         return ResponseHelper::errors(message: 'Unable to update profile: ' . $ex->getMessage(), statusCode: 500);
    //     }
    // }


}
