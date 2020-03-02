package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.basic.MoveCard;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Walpurgisnacht extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Walpurgisnacht.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    private static final RandomizedList<AnimatorCard> spellcasterPool = new RandomizedList<>();

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<magicNumber; i++)
        {
            if (spellcasterPool.Size() == 0)
            {
                spellcasterPool.AddAll(JavaUtilities.Filter(Synergies.GetNonColorlessCard(), c -> c instanceof Spellcaster));
                spellcasterPool.AddAll(JavaUtilities.Filter(Synergies.GetColorlessCards(), c -> c instanceof Spellcaster));
            }

            AnimatorCard spellcaster = spellcasterPool.Retrieve(AbstractDungeon.cardRandomRng, false);
            if (spellcaster != null)
            {
                AbstractCard copy = spellcaster.makeCopy();

                copy.costForTurn = 0;
                copy.isCostModified = true;
                copy.freeToPlayOnce = true;

                GameActions.Bottom.MakeCardInHand(copy);
            }
        }

        GameActions.Bottom.ApplyPower(p, p, new WalpurgisnachtPower(p));
    }

    public static class WalpurgisnachtPower extends AnimatorPower
    {
        public WalpurgisnachtPower(AbstractPlayer owner)
        {
            super(owner, Walpurgisnacht.DATA);

            this.amount = -1;

            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            int numTimesEvoke = AbstractDungeon.player.hand.getCardsOfType(CardType.CURSE).size() + GetSpellcasterCount(AbstractDungeon.player.hand);
            if (numTimesEvoke > 0)
            {
                for (int i = 1; i < numTimesEvoke; i++)
                {
                    GameActions.Bottom.Add(new AnimateOrbAction(1));
                    GameActions.Bottom.Add(new EvokeWithoutRemovingOrbAction(1));
                }

                GameActions.Bottom.Add(new AnimateOrbAction(1));
                GameActions.Bottom.Add(new EvokeOrbAction(1));
            }
        }

        private int GetSpellcasterCount(CardGroup group)
        {
            int count = 0;

            for (AbstractCard card : group.group)
            {
                if (card instanceof Spellcaster)
                {
                    count++;
                }
            }

            return count;
        }
    }
}