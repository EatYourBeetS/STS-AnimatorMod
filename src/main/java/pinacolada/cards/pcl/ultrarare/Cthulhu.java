package pinacolada.cards.pcl.ultrarare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.Cthulhu_Madness;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Cthulhu extends PCLCard_UltraRare
{
    public static final int COST = 12;
    public static final PCLCardData DATA = Register(Cthulhu.class)
            .SetAttack(-1, CardRarity.SPECIAL, PCLAttackType.Brutal, PCLCardTarget.AoE)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.CallOfCthulhu)
            .PostInitialize(data -> data.AddPreview(new Cthulhu_Madness(), false));

    public Cthulhu()
    {
        super(DATA);

        Initialize(800, 0, 12);

        SetAffinity_Star(1, 0, 32);
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

        PCLActions.Bottom.MakeCardInHand(new Cthulhu_Madness());
        PCLActions.Bottom.Flash(this);
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
        PCLActions.Bottom.Callback(() -> {
            for (int i = 0; i < magicNumber; i++) {
                AbstractCard randomCard = PCLGameUtilities.GetAnyColorCardFiltered(null, null, false);
                if (randomCard != null) {
                    PCLGameEffects.TopLevelList.ShowAndObtain(randomCard);
                    PCLActions.Bottom.PlayCopy(randomCard, m);
                }
            }
        }).IsCancellable(false);
        PCLActions.Bottom.VFX(VFX.Cataclysm(), 0.8f, true)
        .AddCallback(__ -> PCLActions.Top.DealCardDamageToAll(this, AttackEffects.NONE));

    }
}