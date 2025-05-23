import React from 'react'
import { LuLoaderCircle } from "react-icons/lu";
const Loader = ({size=18}) => {
  return (
    <LuLoaderCircle size={size} className='animate-spin duration-1000 ease-in-out'/>
  )
}

export default Loader
