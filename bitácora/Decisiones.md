# Decisiones de Arquitectura

## D01 Usar Render como plataforma de despliegue
A lo largo de la carrera, compañeros han usado en proyectos académicos herramientas como Vercel, Netify, Supabase y la verdad no ha pasado nada raro. Sin embargo, como este es un proyecto mucho más serio destinado a uso real, hice una investigación sobre que alternativas tengo por que se de noticias (casos extremos pero reales) como [Netlify just sent me a $104K bill for a simple static site](https://www.reddit.com/r/webdev/comments/1b14bty/netlify_just_sent_me_a_104k_bill_for_a_simple/) y [Vercel April 2026 security incident](https://vercel.com/kb/bulletin/vercel-april-2026-security-incident) que me llevan a desconfiar.

Me tope con Render como una alternativa muy interesante. [Render vs Vercel](https://render.com/docs/render-vs-vercel-comparison) explica que una de las fortalezas de Render viene del enfoque pensado para proyectos Full-stack y alta carga de arquitecturas backend además de ofrecer servicios de redes privadas y TLS orientados a proteger el tráfico de las aplicaciones, protección contra DDoS y las bases de datos gestionadas de Render cuentan de forma nativa con cifrado en reposo: hace la información ilegible ante robos físicos, fugas de datos o accesos lógicos no autorizados. Otro gran atractivo que me convenció de probarlo fue la facilidad que tiene para desplegar aplicaciones y métricas en tiempo real de recursos consumidos; me interesa monitorear como progresa la app con uso real.


---


## D02 Convención de commits: EU Git Guidelines
Buscando tener un *look* más profesional en un proyecto tan importante investigué que forma adoptada en el mundo real existía para hacer commits. Encontré [Git Commit Guidelines](https://ec.europa.eu/component-library/v1.15.0/eu/docs/conventions/git/) y me pareció ordenada y buena forma de organizar el historial de commits. 

También me proporciona una forma eficaz de filtrar commits: citar el requisito (RF/RNF) en el commit da trazabilidad automática. Ejemplo, `git log --grep="RF6"` devuelve todo lo hecho para cumplir la Ley 1/2025.
**Formato**:
```
<tipo>(<ámbito>): <descripción en imperativo y presente>

<cuerpo opcional explicando el qué y el porqué>

Refs: RF3.1 (cuando aplique)
Closes #17  (Pasa issue #17 a terminado)
```

---