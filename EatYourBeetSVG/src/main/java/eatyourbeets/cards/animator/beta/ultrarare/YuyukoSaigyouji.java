package eatyourbeets.cards.animator.beta.ultrarare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.GraveField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.vfx.CherryBlossomEffect;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YuyukoSaigyouji extends AnimatorCard_UltraRare implements StartupCard //TODO
{
    public static final EYBCardData DATA = Register(YuyukoSaigyouji.class).SetPower(3, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.TouhouProject);

    public YuyukoSaigyouji()
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Dark(2, 0, 0);
        SetCostUpgrade(-1);
        SetInnate(true);
        GraveField.grave.set(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Add(new VFXAction(new CherryBlossomEffect(), 0.7F));
        GameActions.Bottom.ApplyPower(new YuyukoSaigyoujiPower(p, magicNumber));
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        for (Affinity af : Affinity.Extended()) {
            GameActions.Bottom.AddAffinity(af, secondaryValue);
        }
        return true;
    }

    public static class YuyukoSaigyoujiPower extends AnimatorPower
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
            for (Affinity af : Affinity.Extended()) {
                if (GameUtilities.GetAffinityLevel(card, af, true) > 0) {
                    increase += CombatStats.Affinities.GetAffinityLevel(af, true);
                }
            }
            return increase;
        }
    }
}