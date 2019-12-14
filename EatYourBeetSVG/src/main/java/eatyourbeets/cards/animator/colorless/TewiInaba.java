package eatyourbeets.cards.animator.colorless;

import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.Earth;
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
            SetExhaust(true);
        }

        @Override
        public void use(AbstractPlayer p, AbstractMonster m)
        {

            GameActions.Top.DiscardFromHand(this.name,1,true);
            GameActions.Bottom.Draw(this.magicNumber);
            if (!p.orbs.isEmpty() && Earth.ORB_ID.equals(p.orbs.get(0).ID) && EffectHistory.TryActivateLimited(cardID))
            {
                GameActions.Bottom.Add(new EvokeSpecificOrbAction(p.orbs.get(0)));
                GameActions.Bottom.ChannelOrb(new Earth(), true);
            }
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
