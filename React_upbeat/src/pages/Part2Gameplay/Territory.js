import React from 'react'
import Link from 'next/link'


function Territory() {
  return (
    <div>
       <li>
        <Link href="/">Home</Link>
      </li>
      <h1>Territory</h1>
      <li>
        <button>
        <Link href="/Part2Gameplay/CurrentConstructionPlan">CurrentConstructionPlan</Link>
        </button>
      </li>
    </div>
  )
}

export default Territory