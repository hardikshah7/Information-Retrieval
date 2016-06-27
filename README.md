Information-Retrieval
=====================

These are the projects I worked on for my Information Retrieval course:
Below is the problem statement for Web Crawler and PageRank algorithm.

1. Web Crawler problem description:
    Implement your own web crawler, with the following properties:

    Be polite and use a delay of at least one second between requests to the web server.
    You should start from the seed document http://en.wikipedia.org/wiki/Gerard_Salton, the Wikipedia article on Gerald Salton,     an important early researcher in information retrieval.
    You should only follow links with the prefix http://en.wikipedia.org/wiki/. In other words, do not follow links to              non-English articles or to non-Wikipedia pages.
    Do not follow links with a colon (:) in the rest of the URL.
    Do not follow links to the main page http://en.wikipedia.org/wiki/Main_Page.
    You may use existing libraries to request documents over HTTP.
    Otherwise, you should implement your own code to extract links, keep track of what you've crawled, and decide what to crawl     next.
    Crawl to at most depth 3 from the seed page. In other words, you should retrieve the seed page, pages it links to, and pages     those pages link to.
    Your crawler should take two arguments: the seed page and an optional "keyphrase" that must be present, in any combination      of upper and lower case, on any page you crawl. If the keyphrase is not present, stop crawling. This is a very simple           version of focused crawling, where the presence or absence of a single feature is used to determine whether a document          is relevant.
    
2. Pagerank algorithm problem description:
    Compute PageRank on a collection of 183,811 web documents.
    Run iterative version of PageRank algorithm until PageRank values "converge". To test for convergence, calculate the            perplexity of the PageRank distribution, where perplexity is simply 2 raised to the (Shannon) entropy of the PageRank           distribution, i.e., 2H(PR). Perplexity is a measure of how "skewed" a distribution is --- the more "skewed" (i.e., less         uniform) a distribution is, the lower its preplexity. Informally, you can think of perplexity as measuring the number of        elements that have a "reasonably large" probability weight; technically, the perplexity of a distribution with entropy h is     the number of elements n such that a uniform distribution over n elements would also have entropy h. (Hence, both               distributions would be equally "unpredictable".)

    Run iterative PageRank algorthm, outputting the perplexity of PageRank distibution until the change in perplexity     is        less than 1 for at least four iterations.
