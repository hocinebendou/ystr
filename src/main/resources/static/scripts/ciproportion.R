# Compute confidence interval

ciproportion <- function(haplocount, totalcount, observed=TRUE) {
    if (haplocount > 0) {
        matchperobserved <-  totalcount %/% haplocount
    }
    if(haplocount == 0) {
        matchperobserved <- (totalcount + 1) %/% (haplocount + 1)
    }
    pbar <- 1 / matchperobserved
    se <- sqrt(pbar * (1 - pbar)/totalcount)
    me <- qnorm(.975) * se
    ci <- 1 / (pbar + c(-me, me))
    if (ci[[1]] < 0 || ci[[1]] > totalcount) {
        if (haplocount == 0) {
            ci[[1]] <- totalcount + 1
        } else {
            ci[[1]] <- totalcount
        }
    }

    return(ci)
}