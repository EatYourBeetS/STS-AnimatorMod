package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.pcl.series.Elsword.Eve;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Eve_Drone extends PCLCard
{
    public static final PCLCardData DATA = Register(Eve_Drone.class)
            .SetAttack(0, CardRarity.SPECIAL, PCLAttackType.Ranged)
            .SetSeries(Eve.DATA.Series);

    public Eve_Drone() {
        this(0);
    }

    public Eve_Drone(int form)
    {
        super(DATA);

        Initialize(3, 0, 1, 0);
        SetAffinity_Silver(1, 0, 0);
        SetRetain(true);
        SetCooldown(4, 2, this::OnCooldownCompleted);
        SetForm(form, timesUpgraded);
    }

    @Override
    public int SetForm(Integer form, int timesUpgraded) {
        if (form == 1) {
            LoadImage("1");
            Initialize(0, 2, 1, 0);
            SetUpgrade(1,1);
        }
        else {
            LoadImage(null);
            Initialize(3, 0, 1, 0);
            SetUpgrade(1,1);
        }
        return super.SetForm(form, timesUpgraded);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (damage > 0) {
            PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.PSYCHOKINESIS).forEach(d -> d.SetVFXColor(Color.CYAN));
        }
        if (block > 0) {
            PCLActions.Bottom.GainBlock(block);
        }
        PCLActions.Last.MoveCard(this,player.hand).AddCallback(() -> {
            PCLGameUtilities.SetUnplayableThisTurn(this);
            cooldown.ProgressCooldownAndTrigger(null);
        });
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        super.triggerOnEndOfTurnForPlayingCard();
        PCLActions.Bottom.GainSupportDamage(magicNumber);
        cooldown.ProgressCooldownAndTrigger(null);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Last.Exhaust(this);
    }
}