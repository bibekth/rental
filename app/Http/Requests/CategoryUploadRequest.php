<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class CategoryUploadRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        if(request()->isMethod('PUT')){
            $rules= 'file|mimes:png,jpg,jpeg,webp';
        }
        elseif(request()->isMethod('POST')){
            $rules= 'required|file|mimes:png,jpg,jpeg,webp' ;

        }
        return [
            'name' => 'required|string|max:255',
            'photo' => $rules,
        ];
    }
    public function messages() : array{
        return [  
           'name.required' => 'Product name is required!',
            'name.string' => 'Product name must be a string.',
            'name.max' => 'Product name cannot exceed 255 characters.',
            'photo.file' => 'Image is required.',
            'photo.mimes' => 'The image must be of type: png, jpg, jpeg, webp.',
        ];
    }
}

