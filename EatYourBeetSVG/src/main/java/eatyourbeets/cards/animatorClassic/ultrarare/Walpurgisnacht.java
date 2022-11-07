package eatyourbeets.cards.animatorClassic.ultrarare;

import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.RandomizedList;

public class Walpurgisnacht extends AnimatorClassicCard_UltraRare
{
    public static final EYBCardData DATA = Register(Walpurgisnacht.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    private static final RandomizedList<AnimatorClassicCard> spellcasterPool = new RandomizedList<>();

    public Walpurgisnacht()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        this.series = CardSeries.MadokaMagica;
        SetSpellcaster();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
//        if (spellcasterPool.Size() == 0)
//        {
//            spellcasterPool.AddAll(JUtils.Filter(Synergies.GetNonColorlessCard(), c -> c.hasTag(SPELLCASTER)));
//            spellcasterPool.AddAll(JUtils.Filter(Synergies.GetColorlessCards(), c -> c.hasTag(SPELLCASTER)));
//        }

        for (int i = 0; i < magicNumber; i++)
        {
            AnimatorClassicCard spellcaster = spellcasterPool.Retrieve(rng, false);
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
                int count = JUtils.Count(player.hand.group, c -> c.type == CardType.CURSE || c.hasTag(AnimatorClassicCard.SPELLCASTER));
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