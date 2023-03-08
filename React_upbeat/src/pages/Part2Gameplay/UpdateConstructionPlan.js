import React from 'react'
import Link from 'next/link'


function UpdateConstructionPlan() {
  return (
    <div><h1>UpdateConstructionPlan</h1>
        <li>
        <p>contructionplan editor</p>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
      <li>
        <p>contructionplan editor</p>
       <textarea
          rows={30}
          cols={100}
        ></textarea>
      </li>
        <li>
             <button>
        <Link href="/Part2Gameplay/CurrentConstructionPlan">finished chenging</Link>
        </button>
        </li>
        
    </div>
  )
}

export default UpdateConstructionPlan