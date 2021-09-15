package eatyourbeets.cards.animator.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.animator.special.Cthulhu_Madness;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class Cthulhu extends AnimatorCard_UltraRare
{
    public static final int COST = 12;
    public static final EYBCardData DATA = Register(Cthulhu.class)
            .SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CallOfCthulhu)
            .PostInitialize(data -> data.AddPreview(new Cthulhu_Madness(), false));

    public Cthulhu()
    {
        super(DATA);

        Initialize(800, 0, 120);

        SetAffinity_Dark(2, 0, 32);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    protected ColoredString GetCostString()
    {
        ColoredString res = super.GetCostString();
        res.text = String.valueOf(COST);
        return res;
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return super.cardPlayable(m) && player.hand.contains(this);
    }

    @Override
    public boolean hasEnoughEnergy()
    {
        this.costForTurn = COST;
        this.freeToPlayOnce = false;
        boolean res = super.hasEnoughEnergy();
        this.costForTurn = -2;
        return res;
    }

    @Override
    public boolean freeToPlay()
    {
        return false;
    }

    @Override
    public boolean canUpgrade()
    {
        return false;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        switch (MathUtils.random(2))
        {
            case 0: SFX.Play(SFX.VO_AWAKENEDONE_1, 0.5f, 0.75f); break;
            case 1: SFX.Play(SFX.VO_AWAKENEDONE_2, 0.5f, 0.75f); break;
            case 2: SFX.Play(SFX.VO_AWAKENEDONE_3, 0.5f, 0.75f); break;
        }

        GameActions.Bottom.MakeCardInHand(new Cthulhu_Madness());
        GameActions.Bottom.Flash(this);
    }

//    @Override
//    public void Refresh(AbstractMonster enemy)
//    {
//        super.Refresh(enemy);
//
//        GameUtilities.ModifyCostForCombat(this, COST, false);
//    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (EnergyPanel.getCurrentEnergy() < COST)
        {
            return;
        }

        EnergyPanel.useEnergy(COST);
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.VFX(VFX.Cataclysm(), 0.8f, true)
        .AddCallback(__ -> GameActions.Top.DealDamageToAll(this, AttackEffects.NONE));
    }
}