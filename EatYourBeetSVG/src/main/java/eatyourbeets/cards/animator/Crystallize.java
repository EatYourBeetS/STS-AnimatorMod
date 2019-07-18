package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.MedicalKit;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Status;

public class Crystallize extends AnimatorCard_Status
{
    public static final String ID = CreateFullID(Crystallize.class.getSimpleName());

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
            CardCrawlGame.sound.play("ORB_FROST_Evoke", 0.2F);
            GameActionsHelper.DamageTarget(p, p, this.secondaryValue/2, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
            GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, this.magicNumber), this.magicNumber);
            GameActionsHelper.DamageTarget(p, p, this.secondaryValue/2, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }
}