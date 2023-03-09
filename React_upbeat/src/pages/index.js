import Head from 'next/head'
import Image from 'next/image'
import { Inter } from 'next/font/google'
import styles from '@/styles/Home.module.css'
import Link from 'next/link'


const inter = Inter({ subsets: ['latin'] })

export default function Home() {
  return (
    <div><h1>homepage</h1>
      
     
      <li>
        <Link href="/HowtoPlay">how to play</Link>
      </li>
      <li>
        <Link href="/Part1SetupGame/SetConfigurationPlan">start game</Link>
      </li>

    </div>
  )
}
