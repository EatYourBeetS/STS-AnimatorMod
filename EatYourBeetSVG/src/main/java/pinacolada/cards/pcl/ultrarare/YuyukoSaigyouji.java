package pinacolada.cards.pcl.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.effects.vfx.CherryBlossomEffect;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class YuyukoSaigyouji extends PCLCard_UltraRare implements StartupCard
{
    public static final PCLCardData DATA = Register(YuyukoSaigyouji.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.TouhouProject);

    public YuyukoSaigyouji()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetCostUpgrade(-1);
        SetInnate(true);
        GraveField.grave.set(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Add(new VFXAction(new CherryBlossomEffect(), 0.7F));
        PCLActions.Bottom.ApplyPower(new YuyukoSaigyoujiPower(p, magicNumber));
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        for (PCLAffinity af : PCLAffinity.Extended()) {
            PCLActions.Bottom.AddAffinity(af, secondaryValue);
        }
        return true;
    }

    public static class YuyukoSaigyoujiPower extends PCLPower
    {
        public YuyukoSaigyoujiPower(AbstractCreature owner, int amount)
        {
            super(owner, YuyukoSaigyouji.DATA);
            Initialize(amount);
        }

        @Override
        public float modifyBlock(float blockAmount, AbstractCard card) {
            return enabled ? blockAmount + GetAffinityIncrease(card) : blockAmount;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            return type == DamageInfo.DamageType.NORMAL ? damage + GetAffinityIncrease(card) : damage;
        }

        private int GetAffinityIncrease(AbstractCard card) {
            int increase = 0;
            for (PCLAffinity af : PCLAffinity.Extended()) {
                if (PCLGameUtilities.GetPCLAffinityLevel(card, af, true) > 0) {
                    increase += PCLCombatStats.MatchingSystem.GetAffinityLevel(af, true);
                }
            }
            return increase;
        }
    }
}