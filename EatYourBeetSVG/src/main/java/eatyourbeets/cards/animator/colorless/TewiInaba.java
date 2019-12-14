package eatyourbeets.cards.animator.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class TewiInaba extends AnimatorCard
    {
        public static final String ID = Register(TewiInaba.class.getSimpleName(), EYBCardBadge.Special);

    public TewiInaba()
        {
            super(ID, 0, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

            Initialize(0,0,2);
            SetSynergy(Synergies.TouhouProject);
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m)
        {
            if(!(EffectHistory.TryActivateLimited(this.cardID) && p.gold >= 200)) {
                this.exhaust = true;
            }

            GameActions.Bottom.Draw(this.magicNumber);
        }

        @Override
        public void upgrade()
        {
            if (TryUpgrade())
            {
                upgradeMagicNumber(1);
            }
        }
    }
