import { ErrorMessage, Form, Formik } from "formik";
import React, { useEffect, useState } from "react";
import { CiMail, CiLock ,CiUser } from "react-icons/ci";
import { getErrorMessage, signinValidation, signupValidation } from "../utils/formikValidation";
import { GoEye, GoEyeClosed } from "react-icons/go";
import { useNavigate } from "react-router-dom";
import { axiosInstance } from "../utils/axiosInstance";
import { useMutation } from "@tanstack/react-query";
import toast from "react-hot-toast";
import Loader from "../components/Loader";

const SignUp = () => {
  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();

   useEffect(()=>{
      
      if(localStorage.getItem("todo-token"))
      {
        navigate("/")
      }
  
    },[])

  const signUp = async (val)=>{
    const res = await axiosInstance.post("/auth/user/public/signup" , val);
    return res.data;
  }

  const signupMutation = useMutation({
    mutationFn:signUp,
    mutationKey : "user-signup",
    onSuccess:(result)=>{
        toast.success("Account Created Successfully");
        const {data} = result;
        const {token}  = data;
        localStorage.setItem("todo-token" , token);
        setTimeout(()=>{
           navigate("/")
        },2000)
    },
    onError:(error)=>{
        toast.error(getErrorMessage(error))
    }
  })


  return (
    <section className="w-full h-screen p-5 flex justify-center items-center font-outfit">
      <article className="w-full max-w-[400px] p-5 shadow-md shadow-gray-500 rounded-2xl">
        <h1 className="text-[1.3rem] md:text-[1.7rem] w-full text-center font-semibold">
          SignUp
        </h1>
        <p className="w-full text-center text-gray-700 text-[0.8rem]">
          
        </p>

        <Formik
          initialValues={{name:"", email: "", password: "" }}
          validationSchema={signupValidation}
          onSubmit={(val) => {
            signupMutation.mutate(val)
          }}
        >
          {({ handleBlur, handleChange }) => (
            <Form className="mt-[40px] w-full">
               <article className="w-full">
                <div className="w-full p-2 flex justify-start items-center border border-slate-200 rounded-xl">
                  <CiUser size={18} />
                  <input
                    type="text"
                    name="name"
                    placeholder="Enter name"
                    className="flex-1 ms-2"
                    onChange={handleChange}
                    onBlur={handleBlur}
                  />
                </div>
                <ErrorMessage
                  name="name"
                  component={"p"}
                  className="text-[0.7rem] text-red-500"
                />
              </article>

              <article className="w-full mt-[10px]">
                <div className="w-full p-2 flex justify-start items-center border border-slate-200 rounded-xl">
                  <CiMail size={18} />
                  <input
                    type="text"
                    name="email"
                    placeholder="Enter the email"
                    className="flex-1 ms-2"
                    onChange={handleChange}
                    onBlur={handleBlur}
                  />
                </div>
                <ErrorMessage
                  name="email"
                  component={"p"}
                  className="text-[0.7rem] text-red-500"
                />
              </article>

              <article className="w-full mt-[10px]">
                <div className="w-full p-2 flex justify-start items-center border border-slate-200 rounded-xl">
                  <CiLock size={18} />
                  <input
                    type={showPassword ? "text" : "password"}
                    name="password"
                    placeholder="Enter password"
                    className="flex-1 ms-2"
                    onChange={handleChange}
                    onBlur={handleBlur}
                  />

                  {!showPassword ? (
                    <GoEye
                      className="cursor-pointer"
                      size={18}
                      onClick={() => setShowPassword(true)}
                    />
                  ) : (
                    <GoEyeClosed
                      className="cursor-pointer"
                      size={18}
                      onClick={() => setShowPassword(false)}
                    />
                  )}
                </div>
                <ErrorMessage
                  name="password"
                  component={"p"}
                  className="text-[0.7rem] text-red-500"
                />
              </article>

              <article className="mt-[20px] w-full flex justify-center items-center">
                <button className="p-2 w-[80%] bg-blue-400 rounded-2xl cursor-pointer flex justify-center items-center gap-2">
                   {
                    signupMutation.isPending && <Loader/>
                   } 
                  Register
                </button>
              </article>
            </Form>
          )}
        </Formik>

        <article className="w-full flex justify-center gap-2 items-center text-[0.7rem] text-gray-500  mt-[20px]">
          <hr className="flex-1" />
          <span>OR</span>
          <hr className="flex-1" />
        </article>

        <article className="w-full mt-[10px] flex justify-center items-center">
          <span className="text-[0.8rem] text-gray-600">
            Already have an account?
            <span className="text-black hover:underline cursor-pointer" onClick={()=>navigate("/signin")}>
              Login
            </span>
          </span>
        </article>
      </article>
    </section>
  );
};

export default SignUp;
