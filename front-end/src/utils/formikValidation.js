import * as YUP from "yup"


export const signinValidation = YUP.object().shape({
    email:YUP.string().required("Required!").email("Invalid Email"),
    password:YUP.string().required("Required")
})

export const signupValidation = YUP.object().shape({
    email:YUP.string().required("Required!").email("Invalid Email"),
    password:YUP.string().required("Required"),
    name:YUP.string().required("Required")
})


export const addTaskValidation = YUP.object().shape({
    title:YUP.string().required("Required!")
})










export const getErrorMessage = (error) => {

//   const {status} = error?.response;

  return error?.response?.data
    ? error?.response?.data?.error
    : "Something Went Wrong!";
};