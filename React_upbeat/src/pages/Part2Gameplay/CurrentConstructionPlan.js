import React from 'react'
import Link from 'next/link'


function CurrentConstructionPlan() {
  return (
    <div>
          <li>
        <Link href="/">Home</Link>
      </li>
      <h1>CurrentConstructionPlan</h1>
      <li>
        <button>
        <Link href="/Part2Gameplay/Territory">Territory</Link>
        </button>
      </li>
      <li>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
      <li>
        <button>
        <Link href="/Part2Gameplay/UpdateConstructionPlan">UpdateConstructionPlan</Link>
        </button>
      </li>
    </div>
  )
}

export default CurrentConstructionPlan