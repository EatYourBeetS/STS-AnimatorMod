package eatyourbeets.cards.animator.beta.Rewrite;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnBattleEndSubscriber;
import eatyourbeets.interfaces.subscribers.OnBattleStartSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class ShizuruNakatsu extends AnimatorCard implements OnBattleStartSubscriber, OnBattleEndSubscriber {
    public static final EYBCardData DATA = Register(ShizuruNakatsu.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Ranged);

    public ShizuruNakatsu() {
        super(DATA);

        Initialize(6, 0, 2);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        final float x = m.hb.cX + (m.hb.width * MathUtils.random(-0.1f, 0.1f));
        final float y = m.hb.cY + (m.hb.height * MathUtils.random(-0.2f, 0.2f));

        GameActions.Bottom.VFX(new ThrowDaggerEffect(x, y));
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);

        GameActions.Bottom.DiscardFromHand(name, magicNumber, !upgraded)
        .ShowEffect(!upgraded, !upgraded)
        .SetOptions(false, false, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        });
    }

    @Override
    public void OnBattleStart() {
        CombatStats.onBattleEnd.Subscribe(this);
    }

    @Override
    public void OnBattleEnd() {
        if (player.stance.ID.equals(AgilityStance.STANCE_ID))
        {
            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, false)));
        }
    }
}