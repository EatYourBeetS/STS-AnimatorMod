package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard_Status;

public class Crystallize extends AnimatorCard_Status
{
    public static final String ID = Register(Crystallize.class.getSimpleName());

    public Crystallize()
    {
        super(ID, 1, CardRarity.UNCOMMON, CardTarget.NONE);

        this.exhaust = true;

        Initialize(0, 0, 4, 6);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        // Do not autoplay
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActionsHelper2.SFX("ORB_FROST_Evoke", 0.2F);
            GameActionsHelper2.LoseHP(secondaryValue/2, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActionsHelper2.GainMetallicize(this.magicNumber);
            GameActionsHelper2.LoseHP(secondaryValue/2, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}