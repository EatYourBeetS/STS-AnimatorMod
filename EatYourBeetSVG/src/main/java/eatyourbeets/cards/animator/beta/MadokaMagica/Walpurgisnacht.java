package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.AnimatorCard_UltraRare;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Walpurgisnacht extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Walpurgisnacht.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    private static final RandomizedList<AnimatorCard> spellcasterPool = new RandomizedList<>();

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetSynergy(Synergies.MadokaMagica);
        SetSpellcaster();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (spellcasterPool.Size() == 0)
        {
            spellcasterPool.AddAll(JUtils.Filter(Synergies.GetNonColorlessCard(), c -> c.hasTag(SPELLCASTER)));
            spellcasterPool.AddAll(JUtils.Filter(Synergies.GetColorlessCards(), c -> c.hasTag(SPELLCASTER)));
        }

        for (int i = 0; i < magicNumber; i++)
        {
            AnimatorCard spellcaster = spellcasterPool.Retrieve(rng, false);
            if (spellcaster != null)
            {
                GameActions.Bottom.MakeCardInHand(spellcaster)
                .SetUpgrade(false, true)
                .AddCallback(c -> c.setCostForTurn(0));
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
            GameActions.Bottom.Callback(() ->
            {
                int count = JUtils.Count(player.hand.group, c -> GameUtilities.IsCurseOrStatus(c) || c.hasTag(AnimatorCard.SPELLCASTER));
                if (count > 0)
                {
                    for (int i = 1; i < count; i++)
                    {
                        GameActions.Bottom.Add(new AnimateOrbAction(1));
                        GameActions.Bottom.Add(new EvokeWithoutRemovingOrbAction(1));
                    }

                    GameActions.Bottom.Add(new AnimateOrbAction(1));
                    GameActions.Bottom.Add(new EvokeOrbAction(1));
                }
            });
        }
    }
}