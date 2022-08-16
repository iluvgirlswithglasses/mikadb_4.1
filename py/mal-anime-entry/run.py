
# dependency: mal-api

from mal import Anime
import sys
import codecs
import os

#
params = []

#
if __name__ == '__main__':
	# import attrs
	with open(os.path.join('py', 'mal-anime-entry', 'attrs.txt'), 'r') as f:
		for line in f:
			params.append(line.rstrip())
	# exec
	anime = Anime(sys.argv[1])
	with codecs.open(os.path.join('py', 'mal-anime-entry', 'out.txt'), 'w', 'utf-8') as f:
		for i in params:
			f.write(i + "../>>>" + str(getattr(anime, i)) + '\n')
