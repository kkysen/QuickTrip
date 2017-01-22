cache = 'requestCache.txt'
filterOut = 'google\\flights'

requests = [request.strip() for request in open(cache) if not filterOut in request]
f = open(cache, 'w')
f.write('\n'.join(requests))
f.close()