package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.HPAttribute;
import pinacolada.cards.pcl.special.FlandreScarlet_RemiliaScarlet;
import pinacolada.effects.AttackEffects;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class FlandreScarlet extends PCLCard
{
    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final PCLCardData DATA = Register(FlandreScarlet.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new FlandreScarlet_RemiliaScarlet(), false));;

    public FlandreScarlet()
    {
        super(DATA);

        Initialize(8, 0);
        SetUpgrade(3, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(PCLAffinity.Dark, 8);

        SetEthereal(true);
    }

    public AbstractAttribute GetSpecialInfo() {
        return HPAttribute.Instance.SetCard(this);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null) {
            CalculateHeal();
        }
        else {
            heal = 0;
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BITE);
        if (CalculateHeal() > 0)
        {
            PCLActions.Bottom.RecoverHP(heal);
        }

        PCLActions.Last.Callback(() -> {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractCard handCard = PCLJUtils.Random(player.hand.group);

                choices.Initialize(this, true);
                choices.AddEffect(new GenericEffect_FlandreScarlet(0, this));
                if (handCard != null) {
                    choices.AddEffect(new GenericEffect_FlandreScarlet(1, handCard));
                }
                choices.Select(1, null);
            }
        });

        PCLActions.Last.Callback(() -> {
            if (CheckAffinity(PCLAffinity.Dark) && info.TryActivateLimited()) {
                PCLActions.Bottom.MakeCardInDrawPile(new FlandreScarlet_RemiliaScarlet());
            }
        });
    }

    protected int CalculateHeal()
    {
        return heal = Math.min(damage, GameActionManager.playerHpLastTurn - player.currentHealth);
    }

    protected static class GenericEffect_FlandreScarlet extends GenericEffect
    {
        private final AbstractCard target;
        public GenericEffect_FlandreScarlet(int amount, AbstractCard target)
        {
            this.amount = amount;
            this.target = target;
        }

        @Override
        public String GetText()
        {
            return FlandreScarlet.DATA.Strings.EXTENDED_DESCRIPTION[amount];
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.Exhaust(target);
        }
    }

}

