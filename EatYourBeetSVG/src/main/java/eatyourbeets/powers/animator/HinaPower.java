package eatyourbeets.powers.animator;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.animator.special.HinaKagiyama;
import eatyourbeets.cards.base.AnimatorCardBuilder;
import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class HinaPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HinaPower.class.getSimpleName());
    public static final int CARD_DRAW_AMOUNT = 2;
    private int baseAmount;
    private EYBCardData data = HinaKagiyama.DATA;
    public HinaPower(AbstractCreature owner, int amount)
    {
        super(owner, HinaKagiyama.DATA);

        this.amount = amount;
        this.baseAmount = amount;
        updateDescription();
    }
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.baseAmount += stackAmount;
        updateDescription();
    }
    @Override
    public void atStartOfTurn()
    {
        this.amount = baseAmount;
        updateDescription();
        if (JavaUtilities.Count(AbstractDungeon.player.exhaustPile.group, GameUtilities::IsCurseOrStatus) > 0){
            GameActions.Bottom.SelectFromPile(name, amount, AbstractDungeon.player.exhaustPile)
                    .SetOptions(false, true)
                    .SetMessage(data.Strings.EXTENDED_DESCRIPTION[4])
                    .SetFilter(GameUtilities::IsCurseOrStatus)
                    .AddCallback(cards ->
                    {
                        for (AbstractCard h : cards)
                        {
                            AbstractDungeon.player.exhaustPile.removeCard(h);

                            AnimatorCardBuilder builder = new AnimatorCardBuilder(data.ID + "_Miracle")
                                    .SetProperties(AbstractCard.CardType.SKILL, data.CardColor, AbstractCard.CardRarity.SPECIAL, AbstractCard.CardTarget.NONE)
                                    .SetImage(data.ImagePath)
                                    .SetCost(0, 0)
                                    .SetNumbers(0, 0, 1, 0)
                                    .SetUpgrades(0, 0, 1, 0)
                                    .SetText(data.Strings.EXTENDED_DESCRIPTION[1], data.Strings.EXTENDED_DESCRIPTION[2], data.Strings.EXTENDED_DESCRIPTION[3],null)
                                    .SetOnUse((c, p, m) -> GameActions.Bottom.GainEnergy(c.magicNumber));
                            EYBCard miracle = builder.Build();
                            miracle.SetPurge(true);
                            miracle.SetRetain(true);
                            GameActions.Bottom.MakeCardInHand(miracle);
                        }
                    });
        }
    }
    @Override
    public void onCardDraw(AbstractCard c)
    {
        if(c.type == AbstractCard.CardType.CURSE && this.amount > 0){
            GameActions.Bottom.Draw(CARD_DRAW_AMOUNT);
            this.amount--;
            this.flash();
        }
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, amount, CARD_DRAW_AMOUNT);;
        this.enabled = (amount > 0);
    }
}

