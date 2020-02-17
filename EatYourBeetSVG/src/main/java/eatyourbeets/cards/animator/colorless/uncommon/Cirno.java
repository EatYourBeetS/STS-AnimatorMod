package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.FallingIceEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Cirno extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cirno.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetColor(CardColor.COLORLESS);

    public Cirno()
    {
        super(DATA);

        Initialize(3, 0);
        SetUpgrade(3, 0);
        SetScaling(1, 0, 0);

        SetEthereal(true);
        SetMultiDamage(true);
        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ChannelOrb(new Frost(), true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Callback(__ ->
        {
            MonsterGroup monsters = AbstractDungeon.getMonsters();
            int frostCount = monsters.monsters.size() + 5;

            for (int i = 0; i < frostCount; i++)
            {
                GameEffects.Queue.Add(new FallingIceEffect(frostCount, monsters.shouldFlipVfx()));
            }
        });

        GameActions.Bottom.ChannelOrb(new Frost(), true);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_VERTICAL).SetOptions(true, false);
    }
}

